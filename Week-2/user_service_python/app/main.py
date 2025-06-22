from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List

from . import models, schemas
from .database import SessionLocal, engine, get_db

models.Base.metadata.create_all(bind=engine)

app = FastAPI()


@app.get("/")
def read_root():
    return {"message": "User service is running"}

@app.put("/api/v1/users/{user_id}", response_model=schemas.User)
def update_user(user_id: int, user_update: schemas.UserUpdate, db: Session = Depends(get_db)):
    """
    Translates the following Java logic:
    
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setEmail(userDto.getEmail());
    
    User updatedUser = userRepository.save(user);
    return modelMapper.map(updatedUser, UserDto.class);
    """
    db_user = db.query(models.User).filter(models.User.id == user_id).first()

    if db_user is None:
        raise HTTPException(status_code=404, detail=f"User with id {user_id} not found")

    # Check if another user already has the new email
    if user_update.email != db_user.email:
        existing_user = db.query(models.User).filter(models.User.email == user_update.email).first()
        if existing_user:
            raise HTTPException(status_code=400, detail=f"Email {user_update.email} is already registered.")

    # Update user fields
    db_user.first_name = user_update.first_name
    db_user.last_name = user_update.last_name
    db_user.email = user_update.email
    
    db.commit()
    db.refresh(db_user)
    
    return db_user

# Placeholder for user creation
@app.post("/api/v1/users/", response_model=schemas.User)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    # This is a simplified creation method for demonstration
    existing_user = db.query(models.User).filter(models.User.email == user.email).first()
    if existing_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    
    # A real implementation should hash the password
    hashed_password = user.password + "notreallyhashed" 
    db_user = models.User(
        email=user.email, 
        first_name=user.first_name, 
        last_name=user.last_name, 
        hashed_password=hashed_password
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user 