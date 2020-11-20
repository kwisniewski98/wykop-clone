package forum.com.Vykop.Config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource pgsqlDataSource(){return DataSourceBuilder.create().build();}

    @Bean(name = "pgsqlJdbcTemplate")
    public JdbcTemplate pgsqljdbcTemplate(@Qualifier("pgsqlDataSource")DataSource pgsqlDataSource){
        return new JdbcTemplate(pgsqlDataSource);
    }

    @Bean(name = "pgsqlJdbcTemplateNamed")
    public NamedParameterJdbcTemplate pgsqljdbcTemplateNamed(@Qualifier("pgsqlDataSource")DataSource pgsqlDataSource){
        return new NamedParameterJdbcTemplate(pgsqlDataSource);
    }

}
