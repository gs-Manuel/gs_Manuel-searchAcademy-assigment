package com.github.gsManuel.APIWEB.service.query;

import com.github.gsManuel.APIWEB.model.QueryRespone;
import com.github.gsManuel.APIWEB.service.elastic.ElasticService;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

public class QueryEngine implements QueryService{

        private ElasticService elasticService;

        public QueryEngine(ElasticService elasticService){
            this.elasticService = elasticService;
        }

        @Override
        public QueryRespone search(String query) {
            String elasticInfo = elasticService.getElasticInfo();
            //Parse the json above to obtain the cluster name
            String clusterName = "";
            try {
                clusterName = new JSONParser(elasticInfo).parseObject().get("cluster_name").toString();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return new QueryRespone(query, clusterName);
        }
    }
