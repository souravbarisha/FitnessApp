🏋️ Fitness App – Full-Stack Microservices Application
📌 Project Overview

The Fitness App is a full-stack microservices-based application designed to track user fitness activities and generate AI-powered recommendations. The system follows a modern cloud-native architecture using Spring Boot microservices, event-driven communication with Apache Kafka, and secure authentication using OAuth2 and Keycloak.

The application allows users to record fitness activities through a web interface, processes this data asynchronously, and leverages Google Gemini AI via Spring AI to provide personalized activity insights and recommendations.

🏗️ System Architecture

The application is built using a microservices architecture with clear separation of responsibilities:

User Service
Manages user profiles and synchronizes user data with Keycloak for authentication and authorization.

Activity Service
Handles creation and storage of user fitness activities. Uses MongoDB for flexible activity data storage and acts as a Kafka producer to publish activity events.

AI Recommendation Service
Consumes activity events from Kafka and uses Spring AI with Google Gemini AI to analyze activity data and generate personalized fitness recommendations.

Service Discovery (Eureka Server)
Enables dynamic service registration and discovery among microservices.

API Gateway (Spring Cloud Gateway)
Acts as a single entry point for all client requests and enforces security and routing rules.

Config Server (Spring Cloud Config)
Centralized configuration management for all microservices.

🔐 Security

Implemented OAuth2 authentication and authorization using Keycloak.

Secured API Gateway to ensure only authenticated users can access backend services.

Integrated Keycloak user identity with backend user data.

📡 Communication & Messaging

Synchronous communication between services using Spring Cloud OpenFeign.

Asynchronous, event-driven communication using Apache Kafka.

Activity Service publishes events to Kafka, which are consumed by the AI Service for recommendation generation.

🎨 Frontend

Built using React with Vite.

Integrates with Keycloak for user authentication.

Provides interfaces for:

User login & authorization

Activity tracking

Viewing activity details and AI-generated recommendations

🗄️ Data Storage

PostgreSQL – User and relational data

MongoDB – Activity and flexible fitness data

Polyglot persistence strategy based on service requirements

🐳 Deployment & DevOps

Dockerized microservices for consistent local development and deployment.

Designed for scalability and easy extension.

🚀 Key Features

User activity tracking

Event-driven microservices architecture

AI-powered fitness recommendations using Gemini AI

Secure authentication with OAuth2 & Keycloak

Kafka-based asynchronous processing

Centralized configuration and service discovery

Modern React-based frontend
