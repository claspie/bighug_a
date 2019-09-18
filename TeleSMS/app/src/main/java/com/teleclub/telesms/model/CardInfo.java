package com.teleclub.telesms.model;

public class CardInfo {
    private String cardName;
    private String countryCode;
    private String localAccessNumber;
    private String internationalPrefix;
    private String separatingCharacters;
    private String pinNumber;
    private String endCharacter;


    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLocalAccessNumber() {
        return localAccessNumber;
    }

    public void setLocalAccessNumber(String localAccessNumber) {
        this.localAccessNumber = localAccessNumber;
    }

    public String getInternationalPrefix() {
        return internationalPrefix;
    }

    public void setInternationalPrefix(String internationalPrefix) {
        this.internationalPrefix = internationalPrefix;
    }

    public String getSeparatingCharacters() {
        return separatingCharacters;
    }

    public void setSeparatingCharacters(String separatingCharacters) {
        this.separatingCharacters = separatingCharacters;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public String getEndCharacter() {
        return endCharacter;
    }

    public void setEndCharacter(String endCharacter) {
        this.endCharacter = endCharacter;
    }
}
