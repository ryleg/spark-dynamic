/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.repl.adaptor

import java.io.BufferedReader

import org.apache.spark._
import org.apache.spark.repl.SparkILoop

object Multiplex {

  /*
export MAVEN_OPTS="-Xmx38g -XX:MaxPermSize=9g -XX:ReservedCodeCacheSize=9g"
export JAVA_HOME="/usr/lib/jvm/java-1.7.0-openjdk-amd64"

export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=512M -XX:ReservedCodeCacheSize=512m"
*/
  def main (args: Array[String]) {

    for (user <- (1 to 10)) {
      Server.start(args, user)
    }

  }
}
