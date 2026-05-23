from fastapi import FastAPI
from contextlib import asynccontextmanager
import uvicorn

from database import db
from routers import router

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Démarrage : Connexion au pool PostgreSQL
    await db.connect()
    yield
    # Arrêt : Déconnexion
    await db.disconnect()

app = FastAPI(title="MatchCraft Python Agent API", version="1.0.0", lifespan=lifespan)

app.include_router(router)

@app.get("/health")
async def health_check():
    return {"status": "ok", "service": "agent-python"}

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
