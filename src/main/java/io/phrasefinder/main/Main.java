// This file is part of PhraseFinder. http://phrasefinder.io
//
// Copyright (C) 2016-2018 Martin Trenkmann
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package io.phrasefinder.main;

import java.io.IOException;
import java.util.Collections;
import org.apache.log4j.Logger;

import io.phrasefinder.Corpus;
import io.phrasefinder.Phrase;
import io.phrasefinder.Phrase.Token;
import io.phrasefinder.PhraseFinder;
import io.phrasefinder.SearchOptions;
import io.phrasefinder.SearchResult;
import io.phrasefinder.SearchResult.Status;

public final class Main {

    private static final String NAME = Main.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    public static void main(String[] args) {

        // Set up your query.
        String query = "I ????";

        // Optional: set the maximum number of phrases to return.
        SearchOptions options = new SearchOptions();
        options.setMaxResults(10);

        // Send the request.
        SearchResult result;
        try {
            LOGGER.info("Searching phrases for query: " + query);
            result = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, query, options);
        } catch (IOException e) {
            return;
        }

        if (result.getStatus() != Status.OK) {
            LOGGER.warn("Request was not successful: " + result.getStatus());
            return;
        }

        // Print phrases line by line.
        for (Phrase phrase : result.getPhrases()) {
            String text = "";
            for (Token token : phrase.getTokens()) {
                text = text.concat(" ").concat(token.getText());
            }
            LOGGER.info(phrase.getScore() + " - " + text);

            Collections.emptyList();
        }
    }
}
