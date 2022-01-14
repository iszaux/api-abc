package com.roshka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PromoItemsDto {

    private BasicDto basic;

    public BasicDto getBasic() {
        return basic;
    }

    public void setBasic(BasicDto value) {
        this.basic = value;

    }

}
