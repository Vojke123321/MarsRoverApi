package com.vojke.MarsRoverApp.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vojke.MarsRoverApp.dto.HomeDto;
import com.vojke.MarsRoverApp.model.MarsPhoto;
import com.vojke.MarsRoverApp.model.MarsRoverModel;

@Service
public class MarsRoverService {

	private static final String API_KEY ="dYs8tUXcdpMNVdXJmPjS9zZi9mbYIUXepp3mvgMH";
;

	private Map<String, List<String>> validCameras = new HashMap<>();


	public MarsRoverService () {
	    validCameras.put("Opportunity", Arrays.asList("FHAZ", "RHAZ", "NAVCAM", "PANCAM", "MINITES"));
	    validCameras.put("Curiosity", Arrays.asList("FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM"));
	    validCameras.put("Spirit", Arrays.asList("FHAZ", "RHAZ", "NAVCAM", "PANCAM", "MINITES"));
	  }

	public MarsRoverModel getMarsRoverData(HomeDto homeDto)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		RestTemplate rt = new RestTemplate();

		List<String> apiUrlEnpoints = getApiUrlEnpoints(homeDto);
		List<MarsPhoto> photos = new ArrayList<>();
		MarsRoverModel response = new MarsRoverModel();

		apiUrlEnpoints.stream().forEach(url -> {
			MarsRoverModel apiResponse = rt.getForObject(url, MarsRoverModel.class);
			photos.addAll(apiResponse.getPhotos());
		});

		response.setPhotos(photos);

		return response;
	}

	public List<String> getApiUrlEnpoints(HomeDto homeDto)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<String> urls = new ArrayList<>();

		Method[] methods = homeDto.getClass().getMethods();

		// This code will grab all isCamera* methods and (if value returns true) then
		// we will build a API URL to
		// call in order to fetch pictures for a given rover / camera / sol.
		for (Method method : methods) {
			if (method.getName().indexOf("isCamera") > -1 && Boolean.TRUE.equals(method.invoke(homeDto))) {
				String cameraName = method.getName().split("isCamera")[1].toUpperCase();
				if (validCameras.get(homeDto.getMarsApiRoverData()).contains(cameraName)) {
					urls.add("https://api.nasa.gov/mars-photos/api/v1/rovers/" + homeDto.getMarsApiRoverData()
							+ "/photos?sol=" + homeDto.getMarsSol() + "&api_key=" + API_KEY + "&camera=" + cameraName);
				}
			}
		}

		return urls;
	}
	
	  public Map<String, List<String>> getValidCameras() {
		    return validCameras;
		  }

}
