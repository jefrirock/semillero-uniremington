# 🎓 Semillero de Investigación - Uniremington Sede Caucasia

[![Java CI with Maven](https://github.com/jefrirock/semillero-uniremington/actions/workflows/maven.yml/badge.svg)](https://github.com/jefrirock/semillero-uniremington/actions/workflows/maven.yml)

## 📋 Descripción del Proyecto

Plataforma web desarrollada para el semillero de investigación de la Universidad Uniremington Sede Caucasia.

**Asignatura:** Lenguaje de Programación Avanzado II  
**Tema:** Automatización de Pruebas y CI/CD

## 🚀 Tecnologías Utilizadas

- **Backend:** Spring Boot 3.x, Java 17
- **Frontend:** Thymeleaf + HTML5 + CSS3 + JavaScript
- **Base de Datos:** H2 (persistente)
- **Pruebas:** JUnit 5 + Mockito
- **CI/CD:** GitHub Actions

## ✨ Funcionalidades Principales

### 1. Gestión de Docentes 👨‍🏫
- CRUD completo con foto de perfil
- Búsqueda por nombre o área

### 2. Gestión de Eventos 📅
- CRUD completo con imagen principal
- Galería de fotos ilimitadas (lightbox)
- Filtrado por categoría
- Eventos dinámicos + estáticos

### 3. Carrusel de Noticias 📰
- Rotación automática cada 5 segundos
- Panel administrativo en `/admin/noticias`

### 4. Efectos Visuales 🎨
- Flip cards 3D en programas académicos
- Modo oscuro toggle
- WhatsApp flotante animado
- Scroll animations

## 🛠️ Instalación y Ejecución

```bash
# Clonar
git clone https://github.com/jefrirock/semillero-uniremington.git

# Compilar
mvn clean package

# Ejecutar
java -jar target/semillero-0.0.1-SNAPSHOT.jar