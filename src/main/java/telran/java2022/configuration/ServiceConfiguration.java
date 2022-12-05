package telran.java2022.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class ServiceConfiguration {
	
		@Bean
		public ModelMapper getModelMapper() {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration()
						.setFieldMatchingEnabled(true)
						.setFieldAccessLevel(AccessLevel.PRIVATE)
						.setMatchingStrategy(MatchingStrategies.STRICT);
			return modelMapper;
		}
}
