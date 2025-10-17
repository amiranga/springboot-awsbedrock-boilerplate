# SpringBoot AWS Bedrock Boilerplate

This is a starter / boilerplate project for integrating **Amazon Bedrock** (via **Spring AI Converse**) into a Spring Boot (Java) application. It provides a minimal setup with a REST endpoint to send prompts and receive responses from a configured Bedrock model (e.g. Nova‑Lite).  

## 🚀 Features

- Spring Boot (Java 21 compatible)  
- Spring AI 1.x integration with Amazon Bedrock Converse  
- REST API endpoint to send a prompt and receive AI-generated text  
- Out-of-the-box configuration for a “cheap” model (Nova‑Lite) to help control cost  
- Supports overriding token limits per request  

---

## 📦 Prerequisites

- Java 21 (or compatible JDK)  
- Maven  
- AWS account with **Bedrock access**, and **model access** enabled  
- IAM role or credentials with permissions:
  - `bedrock:InvokeModel`
  - `bedrock:InvokeModelWithResponseStream`
  - `bedrock:ListModels`
  - `bedrock:GetModel`
  - `aws-marketplace:ViewSubscriptions`, `aws-marketplace:Subscribe`
  
- Bedrock must be available in your chosen region (e.g. `us-east-1`)  
- AWS CLI configured (so Spring AI picks up credentials) **or** your app runs on AWS with an attached IAM role

---

## 🏗️ Project Structure

```
.
├── src
│   └── main
│       ├── java
│       │   └── com.example
│       │       ├── BedrockDemoApplication.java  <-- main class
│       │       └── AiController.java            <-- REST controller
│       └── resources
│           └── application.properties            <-- configuration file
├── pom.xml
└── README.md
```

---

## ⚙️ Configuration (`application.properties`)

Use the following minimal config (you may vary the token limits, temperature, etc.):

```properties
# AWS region
spring.ai.bedrock.aws.region=us-east-1

# Bedrock Converse Chat Model & options
spring.ai.bedrock.converse.chat.options.model=amazon.nova-lite-v1:0
spring.ai.bedrock.converse.chat.options.temperature=0.2
spring.ai.bedrock.converse.chat.options.max-output-tokens=100
```

> **Do not** commit AWS access keys or secrets into this file.  
> Spring AI / AWS SDK will auto-detect credentials via environment, AWS CLI config, or IAM role chain.

You can also override the `maxOutputTokens` per-call from your controller if you want dynamic control.

---

## 🧩 Example Controller (AiController)

The `AiController` exposes:

```java
@PostMapping("/api/ai/ask")
public String ask(@RequestBody PromptDto request) {
    UserMessage userMessage = new UserMessage(request.getPrompt());
    ChatResponse resp = chatModel.call(new Prompt(userMessage).maxOutputTokens(50));
    return resp.getResult().getOutput().getText();
}
```

- Accepts JSON `{ "prompt": "Hello, world" }`  
- Returns the AI-generated text output  

You can adjust or expand this controller to return full metadata (tokens used, costs, etc.)

---

## 📡 Usage / Curl Example

Start the app:

```bash
./mvnw spring-boot:run
```

Then call:

```bash
curl -X POST http://localhost:8080/api/ai/ask      -H "Content-Type: application/json"      -d '{"prompt":"Tell me a joke in one sentence."}'
```

You should receive a JSON/text response from the configured Bedrock model.

---

## 💡 Cost Optimization Tips

- Use **Nova‑Lite** or other “Lite” models for experimentation  
- Keep `max-output-tokens` low (e.g. 50–150)  
- Use short, concise prompts  
- Lower the `temperature` value (e.g. `0.0` to `0.3`)  
- Monitor your usage and set AWS budget alerts  

---

## 🛠️ Troubleshooting

| Issue | Solution |
|-------|-----------|
| `No qualifying bean of type ChatModel` | Ensure the `spring-ai-starter-model-bedrock-converse` dependency is included in `pom.xml`. |
| `AccessDeniedException` | Verify your IAM policy includes `bedrock:InvokeModel` etc., and that your AWS identity is allowed to access the model in Bedrock console. |
| `Model not found` | Confirm the model ID (`amazon.nova-lite-v1:0`) is valid in your region. Use `spring.ai.bedrock.converse.chat.options.model` to adjust. |
| No credentials found | Ensure `aws sts get-caller-identity` works in your environment. Use `aws configure` or assign an IAM role in AWS. |

---

## ✅ Next Steps & Enhancements

- Return richer metadata (usage count, token statistics, cost estimate) in API responses  
- Add user input sanitization / length validation  
- Support streaming responses (if required by your use case)  
- Add caching, rate limiting, or batching logic  
- Add tests (mocking the `ChatModel`)  

---

## 📄 License & Contribution

You may add your preferred license (e.g. MIT) and contribution guidelines.  
Contributions, feedback, and improvements are welcome!
