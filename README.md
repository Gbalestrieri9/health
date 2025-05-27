# 🚑 Health Microservices Project

> Microsserviços para gerenciamento de consultas médicas com comunicação assíncrona.

[![Java](https://img.shields.io/badge/Java-17-brightgreen)](https://www.java.com/) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot) [![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/) [![Docker](https://img.shields.io/badge/Docker-Compose-blue)](https://docs.docker.com/compose/)

---

## 📋 Sumário

- [🌟 Sobre o Projeto](#-sobre-o-projeto)
- [🛠️ Tecnologias](#️-tecnologias)
- [🚀 Começando](#-começando)
- [📦 Docker Compose](#-docker-compose)

---

## 🌟 Sobre o Projeto

Este projeto implementa dois microsserviços em **Spring Boot**:

1. **appointment-service** 🗓️ – CRUD de consultas médicas e publicação de eventos via RabbitMQ.  
2. **notification-service** 🔔 – Consome eventos e envia lembretes automáticos aos pacientes.

A comunicação é feita de forma assíncrona usando **RabbitMQ**, garantindo escalabilidade e resiliência.

---

## 🛠️ Tecnologias

- **Java 17**  
- **Spring Boot** (Web, Data JPA, AMQP)  
- **RabbitMQ** 📥📤  
- **MySQL** / **H2**  
- **Docker & Docker Compose**  
- **JUnit 5 + Mockito + Testcontainers**

---

## 🚀 Começando

1. Clone o repositório:
   ```bash
   git clone https://github.com/Gbalestrieri9/health.git

2. Configuração do Docker:
    ```bash
    # Parar containers em execução (se necessário)
    docker-compose down
    
    # Remover volumes antigos (se necessário)
    docker-compose down -v
    
    # Construir e iniciar os containers
    docker-compose up --build -d
    
    # Verificar logs
    docker-compose logs appointment-service
    
    # Verificar status dos containers
    docker ps

## 📦 Docker Compose

 Levanta 4 containers:

- mysql (8306) – Banco de dados persistente

- rabbitmq (5672, 15672) – Broker de mensagens

- appointment-service (8081) – Microsserviço de agendamento

- notification-service (8082) – Microsserviço de notificações

