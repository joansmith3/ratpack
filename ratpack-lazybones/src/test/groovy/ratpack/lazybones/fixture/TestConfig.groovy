/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.lazybones.fixture

class TestConfig {

  Properties testConfigProperties

  TestConfig() {
    def testConfigResourcePath = "/test-config.properties"
    def testConfigResourceStream = getClass().getResourceAsStream(testConfigResourcePath)
    if (!testConfigResourceStream) {
      throw new RuntimeException("Test config properties resource file not found at $testConfigResourcePath. Run 'writeTestConfig' task to generate it.")
    }
    testConfigProperties = new Properties()
    testConfigProperties.load(testConfigResourceStream)
  }

  File getTemplateDirectory() {
    new File(testConfigProperties["template.path"])
  }

  String getLocalRepoUrl() {
    testConfigProperties["localRepo.url"]
  }
}
