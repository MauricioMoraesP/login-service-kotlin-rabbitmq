package com.login.demo.configurations

   import org.springframework.amqp.core.Queue
  import org.springframework.amqp.rabbit.connection.ConnectionFactory
  import org.springframework.amqp.rabbit.core.RabbitAdmin
  import org.springframework.amqp.rabbit.core.RabbitTemplate
  import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
  import org.springframework.boot.context.event.ApplicationReadyEvent
  import org.springframework.context.ApplicationListener
  import org.springframework.context.annotation.Bean
  import org.springframework.context.annotation.Configuration


@Configuration
class RabbitMQConfig {
    @Bean
    public fun  queue():Queue{
        return Queue("users.v1.user-validate")
    }

    @Bean
 public   fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        return RabbitAdmin(connectionFactory)
    }

    @Bean
    fun applicationReadyEventApplicationListener(rabbitAdmin:RabbitAdmin): ApplicationListener<ApplicationReadyEvent> {
        return ApplicationListener {
            rabbitAdmin.initialize()
        }
    }

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }


    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory, messageConververt:Jackson2JsonMessageConverter): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}