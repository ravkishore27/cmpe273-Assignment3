package wallet;

import java.util.*;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@RestController
public class CounterController {

		private int counter = 0;

		//Healthcheck
		@RequestMapping(value="/healthcheck", method=RequestMethod.GET)
		@ResponseBody
		public String healthcheck(){
			return "Success!";
		}


		//CounterReset
		@RequestMapping(value="/api/v1/counterreset", method=RequestMethod.GET)
		@ResponseBody
		public String setCounterReset(){
			try{
						Runtime.getRuntime().exec("curl -L http://54.67.13.253:4001/v2/keys/009433996/counter -XPUT -d value=0");
			}
			catch(IOException e){
				System.out.println(e);
			}
			return "Success!";
		}

		//CounterAPI
		@RequestMapping(value="/api/v1/counter", method=RequestMethod.GET)
		@ResponseBody
		public String getCounter(){
			RestTemplate restTemplate = new RestTemplate();
			String str = restTemplate.getForObject("http://54.67.13.253:4001/v2/keys/009433996/counter",String.class);
			System.out.println(str);
			JSONParser jsonParser = new JSONParser();
			try{
				JSONObject jsonObject = (JSONObject)jsonParser.parse(str);
				jsonObject = (JSONObject) (jsonObject.get("node"));
				System.out.println(jsonObject);
				counter = Integer.parseInt(((String)jsonObject.get("value")));
				counter = counter + 1;
				System.out.println(Runtime.getRuntime().exec("curl -L http://54.67.13.253:4001/v2/keys/009433996/counter -XPUT -d value=" + counter));
			}
			catch(ParseException e){
				System.out.println(e);
			}
			catch(IOException e){
				System.out.println(e);
			}

			return "Counter : " + counter;
		}
}
