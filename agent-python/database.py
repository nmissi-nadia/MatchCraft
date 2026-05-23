import os
import asyncpg
from contextlib import asynccontextmanager

DB_HOST = os.getenv("DB_HOST", "db-postgres")
DB_PORT = os.getenv("DB_PORT", "5432")
DB_NAME = os.getenv("DB_NAME", "matchcraft")
DB_USER = os.getenv("DB_USER", "matchcraft_user")
DB_PASSWORD = os.getenv("DB_PASSWORD", "matchcraft_password")

DATABASE_URL = f"postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}"

class Database:
    def __init__(self):
        self.pool = None

    async def connect(self):
        self.pool = await asyncpg.create_pool(DATABASE_URL)
        print("Connected to PostgreSQL pool.")

    async def disconnect(self):
        if self.pool:
            await self.pool.close()
            print("Disconnected from PostgreSQL pool.")

db = Database()

@asynccontextmanager
async def get_db_connection():
    if not db.pool:
        raise Exception("Database pool is not initialized.")
    async with db.pool.acquire() as connection:
        yield connection
