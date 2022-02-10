package uj.java.w7.insurance;

import java.math.BigDecimal;

public record InsuranceEntry(
        String policyId, String stateCode, String county,
        String eqSiteLimit, String huSiteLimit, String flSiteLimit,
        String frSiteLimit, BigDecimal tiv2011, BigDecimal tiv2012,
        String eqSiteDeductible, String huSiteDeductible, String flSiteDeductible,
        String frSiteDeductible, String pointLatitude, String pointLongitude,
        String line, String construction, String pointGranularity) {

    @Override
    public String toString() {
        return "InsuranceEntry{" +
                "policyId='" + policyId + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", county='" + county + '\'' +
                ", eqSiteLimit='" + eqSiteLimit + '\'' +
                ", huSiteLimit='" + huSiteLimit + '\'' +
                ", flSiteLimit='" + flSiteLimit + '\'' +
                ", frSiteLimit='" + frSiteLimit + '\'' +
                ", tiv2011=" + tiv2011 +
                ", tiv2012=" + tiv2012 +
                ", eqSiteDeductible='" + eqSiteDeductible + '\'' +
                ", huSiteDeductible='" + huSiteDeductible + '\'' +
                ", flSiteDeductible='" + flSiteDeductible + '\'' +
                ", frSiteDeductible='" + frSiteDeductible + '\'' +
                ", pointLatitude='" + pointLatitude + '\'' +
                ", pointLongitude='" + pointLongitude + '\'' +
                ", line='" + line + '\'' +
                ", construction='" + construction + '\'' +
                ", pointGranularity='" + pointGranularity + '\'' +
                '}';
    }
}
