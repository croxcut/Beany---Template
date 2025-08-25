#include <jni.h>
#include <string>
#include "llama.h"

static llama_context *ctx = nullptr;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_tinyllama_LlamaBridge_initModel(JNIEnv *env, jobject thiz, jstring modelPath) {
const char *path = env->GetStringUTFChars(modelPath, 0);

llama_model_params params = llama_model_default_params();
llama_model *model = llama_load_model_from_file(path, params);

llama_context_params ctx_params = llama_context_default_params();
ctx = llama_new_context_with_model(model, ctx_params);

env->ReleaseStringUTFChars(modelPath, path);
}

extern "C"
JNIEXPORT jstring JNICALL
        Java_com_example_tinyllama_LlamaBridge_generateText(JNIEnv *env, jobject thiz, jstring prompt) {
const char *input = env->GetStringUTFChars(prompt, 0);

std::string result;

// Simple inference (one batch)
llama_batch batch = llama_batch_init(512, 0, 1);
llama_token tokens[512];
int n = llama_tokenize(ctx, input, tokens, 512, true, false);
llama_batch_add(&batch, tokens, n, 0, true);

llama_decode(ctx, batch);

for (int i = 0; i < 50; i++) { // generate max 50 tokens
int token = llama_sample_top_p_top_k(ctx, nullptr, 0.9f, 40, nullptr);
if (token == llama_token_eos(ctx)) break;

result += llama_token_to_str(ctx, token);
llama_batch_clear(&batch);
llama_batch_add(&batch, &token, 1, i+1, true);
llama_decode(ctx, batch);
}

llama_batch_free(batch);
env->ReleaseStringUTFChars(prompt, input);

return env->NewStringUTF(result.c_str());
}
