package com.example.todaysbook.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryEnum {
    IMPROVEMENT("101","자기계발"),
    CHILDREN("102","어린이"),
    FOREIGN("103","외국어"),
    HUMANITIES("104","인문학"),
    BABY("105","유아"),
    CARTOON("106","만화"),
    ECONOMIC("107","경제경영"),
    NOVEL("108","소설/시/희곡"),
    ESSAY("109","에세이"),
    CERTIFICATE("110","수험서/자격증"),
    ART("111","예술/대중문화"),
    COMPUTER("112","컴퓨터/모바일"),
    TEENAGER("113","청소년"),
    SCIENCE("114","과학"),
    SOCIAL_SCIENCE("115","사회과학"),
    PARENT("116","좋은부모"),
    HEALTH("117","건강/취미"),
    HISTORY("118","역사"),
    RELIGION("119","종교/역학"),
    COOKING("120","요리/살림"),
    TRAVEL("121","여행"),
    PROFESSIONAL("122","대학교재/전문서적"),
    ETC("123","기타");

    private final String key;
    private final String title;
}
