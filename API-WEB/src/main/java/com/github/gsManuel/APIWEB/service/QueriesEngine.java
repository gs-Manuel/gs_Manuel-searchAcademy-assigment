package com.github.gsManuel.APIWEB.service;

import co.elastic.clients.elasticsearch.sql.QueryResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

public class QueriesEngine implements QueryService{

        private ElasticService elasticService;

        public QueriesEngine(ElasticEngine elasticEngine){
            this.elasticService = elasticService;
        }

        @Override
        public QueryResponse search(String query) {
            String elasticInfo = ElasticService.getElasticInfo();
            //Parse the json above to obtain the cluster name
            String clusterName = "";
            try {
                clusterName = new JSONParser(elasticInfo).parseObject().get("cluster_name").toString();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return new QueryResponse(query, clusterName);
        }
    }
}
