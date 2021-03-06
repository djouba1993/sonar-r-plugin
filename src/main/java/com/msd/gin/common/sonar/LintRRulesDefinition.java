/*
 * Copyright 2018 Merck Sharp & Dohme Corp. a subsidiary of Merck & Co.,
 * Inc., Kenilworth, NJ, USA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msd.gin.common.sonar;

import com.msd.gin.common.sonar.language.RLanguage;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LintRRulesDefinition implements RulesDefinition {

    public static final String REPO_KEY = RLanguage.KEY + "-" + "lintr";
    public static final String REPO_NAME = "LintR";

    private static final String PATH = "lintr-rules.xml";
    private final RulesDefinitionXmlLoader xmlLoader;

    public static List<String> RULE_KEYS = Collections.emptyList();

    public LintRRulesDefinition(RulesDefinitionXmlLoader xmlLoader) {
        this.xmlLoader = xmlLoader;
    }

    @Override
    public void define(Context context) {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(PATH), StandardCharsets.UTF_8)) {
            NewRepository repository = context.createRepository(REPO_KEY, RLanguage.KEY).setName(REPO_NAME);
            xmlLoader.load(repository, reader);
            List<String> allKeys = repository.rules().stream().map(NewRule::key).collect(Collectors.toList());
            RULE_KEYS = allKeys;
            repository.done();
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Fail to read file %s", PATH), e);
        }

    }

}
