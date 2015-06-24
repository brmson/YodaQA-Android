package in.vesely.eclub.yodaqa.restclient;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by vesely on 6/15/15.
 */
@Rest(rootUrl = "http://live.ailao.eu/", converters = {
        MappingJackson2HttpMessageConverter.class,
        FormHttpMessageConverter.class,
        StringHttpMessageConverter.class})
public interface YodaRestClient extends RestClientErrorHandling {

    @Get("/q/{id}")
    @Accept(MediaType.APPLICATION_JSON)
    YodaAnswersResponse getResponse(String id);

    @Post("/q")
    @RequiresHeader("Content-Type")
    String getId(MultiValueMap data);

    void setHeader(String name, String value);
    public RestTemplate getRestTemplate();
}

