package com.urlShortner;

import java.nio.charset.StandardCharsets;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.hash.Hashing;

@RestController
@RequestMapping("/restapi/url")
public class URLResourceMappingController {
	
	@Autowired
	StringRedisTemplate redisTemplate;
	
	@GetMapping("/{alis}")
	public String getLongURLFromAlis(@PathVariable String alis) {
		String longUrl = redisTemplate.opsForValue().get(alis);
		return longUrl;
	}
	
	@PostMapping
	public String createShortURL( @RequestBody String longUrl)
	{
		String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
		UrlValidator urlValidator = new UrlValidator(schemes);
		
		if (urlValidator.isValid(longUrl)) {
			String alis = Hashing.murmur3_32().hashString(longUrl, StandardCharsets.UTF_8).toString();
			redisTemplate.opsForValue().set(alis, longUrl);
		
			return alis;
		} 
	   System.out.println("URL is invalid");
//	   throw new RuntimeException("Invalid url");
	   return "Invalid url";
	}
}
