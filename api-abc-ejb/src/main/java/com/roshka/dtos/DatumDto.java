package com.roshka.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatumDto {

    @JsonProperty("_website_urls")
    private List<String> websiteUrls; // urls
    private DescriptionDto headlines; // titulo de la noticia
    @JsonProperty("promo_items")
    private PromoItemsDto promoItems; // foto
    @JsonProperty("publish_date")
    private String publishDate; // fecha y hora de la noticia
    private DescriptionDto subheadlines;// resumen de la noticia

    public List<String> getWebsiteUrls() {
        return websiteUrls;
    }

    public void setWebsiteUrls(List<String> value) {
        this.websiteUrls = value;
    }

    public DescriptionDto getHeadlines() {
        return headlines;
    }

    public void setHeadlines(DescriptionDto value) {
        this.headlines = value;
    }

    public PromoItemsDto getPromoItems() {
        return promoItems;
    }

    public void setPromoItems(PromoItemsDto value) {
        this.promoItems = value;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String value) {
        this.publishDate = value;
    }

    public DescriptionDto getSubheadlines() {
        return subheadlines;
    }

    public void setSubheadlines(DescriptionDto value) {
        this.subheadlines = value;
    }

}
