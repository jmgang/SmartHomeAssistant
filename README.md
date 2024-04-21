# Smart Home Assistant

## Overview
Smart Home Assistant is a robust home automation solution designed to enhance everyday living through the integration of advanced AI technologies. Utilizing langchain4j for system architecture, this smart assistant leverages Amazon Transcribe for text-to-speech (TTS) capabilities and Amazon Polly for speech-to-text (STT) functionalities. At its core, it employs OpenAI's GPT-4 for conversational intelligence and Gemini Pro 1.5 for advanced image understanding.
This was part of the demonstration for the Advanced Minds Conference 2024: IoT Trends and Innovations.

## Features
- **Voice and Text Interaction**: Interact using both voice and text through Amazon Transcribe and Amazon Polly, offering seamless communication.
- **Image Understanding**: Powered by Gemini Pro 1.5, the assistant can interpret and analyze images from a USB webcam, making it highly suitable for a variety of practical home applications.
- **Smart Home Control**: Integrates with HomeAssistant API to manage home devices such as smart strips, providing a centralized control over your home automation systems.
- **News and Weather Updates**: Stay informed with the latest news and weather forecasts, ensuring you're always updated with essential information.
- **Generative AI and Machine Learning**: Implements multimodal LLMs, moderation LLMs, and retrieval-augmented generation for enhanced interaction. It utilizes prompt engineering and LLM intent classification to refine and optimize responses.

## Technologies Used
- **Langchain4j**: Framework for building the assistant's architecture.
- **Amazon Transcribe**: For converting speech to text.
- **Amazon Polly**: For converting text to speech.
- **OpenAI GPT-4**: Provides the underlying AI and conversational intelligence.
- **Gemini Pro 1.5**: For image processing and understanding.
- **Weaviate**: Acts as a vector store to support the system's question-answering capabilities through RAG (Retrieval Augmented Generation).
- **OpenAI Moderation Models**: Ensures inputs and interactions are moderated for safety and appropriateness.

## Getting Started
To get started with the Smart Home Assistant, clone this repository and follow the setup instructions below:
```bash
git clone https://github.com/yourusername/smarthome-assistant.git
cd smarthome-assistant

## License
Distributed under the MIT License. See LICENSE for more information.
