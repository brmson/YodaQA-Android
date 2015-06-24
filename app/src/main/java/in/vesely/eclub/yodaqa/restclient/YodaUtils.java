package in.vesely.eclub.yodaqa.restclient;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by vesely on 6/15/15.
 */
public class YodaUtils {
    public static MultiValueMap<String, String> getQuestionPostData(String question){
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("text", question);
        return map;
    }

    public static void setContentType(YodaRestClient restClient){
        restClient.setHeader("Content-Type","application/x-www-form-urlencoded");
    }
}
