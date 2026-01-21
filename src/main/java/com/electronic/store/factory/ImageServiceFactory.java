package com.electronic.store.factory;

import com.electronic.store.services.imageService.ImageService;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class ImageServiceFactory {
    private Map<String, ImageService> imageServiceMap;

    public ImageServiceFactory(Map<String, ImageService> imageServiceMap) {
        this.imageServiceMap = imageServiceMap;
    }

    public ImageService getImageService(String serviceType){
        ImageService imageService = imageServiceMap.get(serviceType);
        return imageService;
    }
}
