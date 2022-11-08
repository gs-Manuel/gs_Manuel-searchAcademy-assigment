package com.github.gsManuel.APIWEB.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

 /*   @Test
    void givenQueryWithResults_whenSearch_thenReturnNonZeroNumFound() {
        String query = "query with results";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.search(query)).willReturn(5);

        SearchService searchService = new SearchService(searchEngine);

        int numResults = searchService.search(query);

        assertTrue(numResults > 0);
    }

    @Test
    void givenQueryWithNoResults_whenSearch_thenReturnZeroNumFound() {
        String query = "query with no results";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.search(query)).willReturn(0);

        SearchService searchService = new SearchService(searchEngine);

        int numResults = searchService.search(query);

        assertEquals(0, numResults);
    }
    @Test
    void givenNoQuery_whenSearch_thenPropagateError() {
        SearchEngine searchEngine = mock(SearchEngine.class);
        Throwable expectedException = new RuntimeException("Error while searching");
        given(searchEngine.search(null)).willThrow(expectedException);

        ElasticService searchService = new ElasticService(searchEngine);

        assertThrows(expectedException.getClass(), () -> searchService.search(null));
    }

  */
}
