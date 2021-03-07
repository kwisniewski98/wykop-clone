package forum.com.Vykop.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class IgnoreHibernatePropertiesInJackson{ }