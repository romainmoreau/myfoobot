package fr.romainmoreau.myfoobot;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiClient {
	private static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApiProperties apiProperties;

	private String xAuthToken;

	@PostConstruct
	private void login() {
		if (xAuthToken == null) {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Authorization",
					"Basic " + new String(
							Base64.encodeBase64((apiProperties.getUsername() + ":" + apiProperties.getPassword())
									.getBytes(Charset.forName("US-ASCII")))));
			HttpEntity<?> httpEntity = new HttpEntity<>(null, httpHeaders);
			ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
					"https://api.foobot.io/v2/user/{username}/login/", HttpMethod.GET, httpEntity, Boolean.class,
					apiProperties.getUsername());
			xAuthToken = responseEntity.getHeaders().getFirst(X_AUTH_TOKEN);
		}
	}

	private <Request, Response> Response execute(String url, HttpMethod method, Request request,
			Class<Response> responseType, Object... uriVariables) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(X_AUTH_TOKEN, xAuthToken);
		HttpEntity<Request> httpEntity = new HttpEntity<>(request, httpHeaders);
		ResponseEntity<Response> responseEntity = restTemplate.exchange(url, method, httpEntity, responseType,
				uriVariables);
		return responseEntity.getBody();
	}

	public Device[] getDevices() {
		return execute("https://api.foobot.io/v2/owner/{username}/device/", HttpMethod.GET, null, Device[].class,
				apiProperties.getUsername());
	}

	public DataPoints getLastDataPoints(String uuid, int period, int sampling) {
		return execute("https://api.foobot.io/v2/device/{uuid}/datapoint/{period}/last/{sampling}/", HttpMethod.GET,
				null, DataPoints.class, uuid, period, sampling);
	}
}
