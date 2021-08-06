package com.vaibhav.healthify.data.models.mapper

import com.vaibhav.healthify.data.models.local.User
import com.vaibhav.healthify.data.models.remote.UserDTO

class UserMapper : Mapper<User, UserDTO> {
    override fun toEntity(dto: UserDTO): User {
        return User(
            username = dto.username,
            email = dto.email,
            profileImg = dto.profileImg,
            exp = dto.exp
        )
    }

    override fun toEntityList(dtos: List<UserDTO>): List<User> {
        return dtos.map {
            toEntity(it)
        }
    }

    override fun toDTO(entity: User): UserDTO {
        return UserDTO(
            username = entity.username,
            email = entity.email,
            profileImg = entity.profileImg,
            exp = entity.exp
        )
    }
}