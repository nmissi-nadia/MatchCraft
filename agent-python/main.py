from fastapi import FastAPI
import uvicorn

app = FastAPI(title="MatchCraft Python Agent API", version="1.0.0")

@app.get("/health")
async def health_check():
    return {"status": "ok", "service": "agent-python"}

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
