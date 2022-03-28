package com.DeBM.ApiDeBM.dto;

import lombok.*;

public interface MenuDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class MenuItem implements MenuDTO{
        public String title;
        public String icon;
        public String link;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class MenuSpacer implements MenuDTO{
        public String title;
        public boolean group;
    }
}
