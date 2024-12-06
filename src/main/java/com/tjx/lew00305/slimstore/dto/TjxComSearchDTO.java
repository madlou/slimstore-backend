package com.tjx.lew00305.slimstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TjxComSearchDTO {

    public static class Metadata {
        
        public static class Query {
            
            public static class Precision {
                
                public static class StringValue {
                    
                    public String value;
                    
                }

                public StringValue configured;
                public StringValue applied;
                
            }
            
            public Precision precision;
            
        }
        
        public Query query;
        
    }

    public class Response {
        
        public static class Doc {
            
            public static class Variant {
                
                public String skuid;
                
            }

            public String description;
            public float price;
            public String url;
            public String pid;
            public String title;
            @JsonProperty("thumb_image")
            public String thumbImage;
            public String brand;
            @JsonProperty("primary_category_code")
            public String primaryCategoryCode;
            @JsonProperty("published_days")
            public int publishedDays;
            @JsonProperty("is_shy_brand")
            public String isShyBrand;
            @JsonProperty("is_bundle")
            public String isBundle;
            @JsonProperty("primary_category_name")
            public String primaryCategoryName;
            @JsonProperty("mh_class")
            public String mhClass;
            public String code;
            @JsonProperty("mh_dept_name")
            public String mhDeptName;
            @JsonProperty("fmt_was_price")
            public String fmtWasPrice;
            @JsonProperty("fmt_rrp")
            public String fmtRrp;
            @JsonProperty("primary_category_path")
            public String primaryCategoryPath;
            @JsonProperty("style_name")
            public String styleName;
            @JsonProperty("mhClass_name")
            public String mhClassName;
            @JsonProperty("published_date")
            public int publishedDate;
            @JsonProperty("fmt_price")
            public String fmtPrice;
            @JsonProperty("fmt_save_price")
            public String fmtSavePrice;
            @JsonProperty("save_price")
            public int savePrice;
            public int rrp;
            public int stock;
            @JsonProperty("bundle_skuid")
            public String bundleSkuid;
            @JsonProperty("percent_saving")
            public int percentSaving;
            @JsonProperty("was_price")
            public int wasPrice;
            @JsonProperty("is_low_stock")
            public String isLowStock;
            @JsonProperty("mh_dept")
            public String mhDept;
            public String department;
            
            public Variant[] variants;
            
        }

        public static class FacetCounts {
            
            public static class FacetFields {
                
                public static class NameCount {
                    
                    public String name;
                    public int count;
                    
                }

                @JsonProperty("Brand")
                public NameCount[] brand;
                @JsonProperty("Department")
                public NameCount[] department;
                @JsonProperty("Size")
                public NameCount[] size;
                @JsonProperty("Style")
                public NameCount[] style;
                @JsonProperty("Price")
                public NameCount[] price;
                @JsonProperty("Colour")
                public NameCount[] colour;
                @JsonProperty("Material")
                public NameCount[] material;
                
                @JsonProperty("Clearance")
                public NameCount[] clearance;
                
            }

            public static class FacetQueries {
                
            }

            public static class FacetRanges {
                
                public static class Price {
                    
                    public String start;
                    public String end;
                    public String count;
                    
                }
                
                @JsonProperty("Price")
                public Price[] price;
                
            }
            
            public FacetFields facetFields;
            
            @JsonProperty("facet_queries")
            public FacetQueries facetQueries;
            
            @JsonProperty("facet_ranges")
            public FacetRanges facetRanges;
            
        }

        public int numFound;
        public int start;
        
        public Doc[] docs;
        
        @JsonProperty("facet_counts")
        public FacetCounts facetCounts;
        
    }

    public static class Stats {
        
        public static class StatFields {
            
            public static class Price {
                
                public Double min;
                public Double max;
                
            }
            
            public Price price;
            
        }
        
        @JsonProperty("")
        public StatFields stats_fields;
        
    }

    public Response response;
    @JsonProperty("category_map")
    public Object categoryMap;
    
    public Stats stats;
    
    @JsonProperty("did_you_mean")
    public String[] didYouMean;
    
    public Metadata metadata;
    
}
