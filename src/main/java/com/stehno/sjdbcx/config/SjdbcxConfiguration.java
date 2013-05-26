/*
 * Copyright (c) 2013 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.sjdbcx.config;

import com.stehno.sjdbcx.IndexedParamMapper;
import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.support.SqlResolver;
import com.stehno.sjdbcx.SqlTransformer;
import com.stehno.sjdbcx.reflection.DefaultIndexedParamMapper;
import com.stehno.sjdbcx.reflection.DefaultParamMapper;
import com.stehno.sjdbcx.reflection.extractor.AnnotatedCollaborationExtractor;
import com.stehno.sjdbcx.reflection.extractor.CollaboratorExtractor;
import com.stehno.sjdbcx.reflection.extractor.RowMapperExtractor;
import com.stehno.sjdbcx.reflection.extractor.SqlTransformerExtractor;
import com.stehno.sjdbcx.reflection.operation.OperationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * This configuration bean provides the default beans for the SJDBCX beans and resolvers.
 *
 * A javax.sql.DataSource named "dataSource" must be defined somewhere in the application.
 */
@Configuration
public class SjdbcxConfiguration {

    @Autowired private DataSource dataSource;

    @Bean
    public SqlResolver sqlResolver(){
        return new SqlResolver();
    }

    @Bean
    public OperationContext operationContext(){
        // TODO: see if there is a better way to do this with less config
        final OperationContext operationContext = new OperationContext();
        operationContext.setDataSource( dataSource );
        operationContext.registerExtractor( RowMapper.class, rowMapperExtractor() );
        operationContext.registerExtractor( ParamMapper.class, paramMapperExtractor() );
        operationContext.registerExtractor( IndexedParamMapper.class, indexedParamMapperExtractor() );
        operationContext.registerExtractor( PreparedStatementCallback.class, preparedStatementCallbackExtractor() );
        operationContext.registerExtractor( PreparedStatementSetter.class, preparedStatementSetterExtractor() );
        operationContext.registerExtractor( ResultSetExtractor.class, resultSetExtractorExtractor() );
        operationContext.registerExtractor( SqlTransformer.class, sqlTransformerExtractor() );
        return operationContext;
    }

    // collaborator extractors

    @Bean
    public CollaboratorExtractor<RowMapper> rowMapperExtractor(){
        return new RowMapperExtractor();
    }

    @Bean
    public CollaboratorExtractor<ParamMapper> paramMapperExtractor(){
        return new AnnotatedCollaborationExtractor<>(
            com.stehno.sjdbcx.annotation.ParamMapper.class,
            (ParamMapper)new DefaultParamMapper()
        );
    }

    @Bean
    public CollaboratorExtractor<IndexedParamMapper> indexedParamMapperExtractor(){
        return new AnnotatedCollaborationExtractor<>(
            com.stehno.sjdbcx.annotation.IndexedParamMapper.class,
            (IndexedParamMapper)new DefaultIndexedParamMapper()
        );
    }

    @Bean
    public CollaboratorExtractor<PreparedStatementCallback> preparedStatementCallbackExtractor(){
        return new AnnotatedCollaborationExtractor<>( com.stehno.sjdbcx.annotation.PreparedStatementCallback.class );
    }

    @Bean
    public CollaboratorExtractor<PreparedStatementSetter> preparedStatementSetterExtractor(){
        return new AnnotatedCollaborationExtractor<>( com.stehno.sjdbcx.annotation.PreparedStatementSetter.class );
    }

    @Bean
    public CollaboratorExtractor<ResultSetExtractor> resultSetExtractorExtractor(){
        return new AnnotatedCollaborationExtractor<>( com.stehno.sjdbcx.annotation.ResultSetExtractor.class );
    }

    @Bean
    public CollaboratorExtractor<SqlTransformer> sqlTransformerExtractor(){
        return new SqlTransformerExtractor();
    }
}
