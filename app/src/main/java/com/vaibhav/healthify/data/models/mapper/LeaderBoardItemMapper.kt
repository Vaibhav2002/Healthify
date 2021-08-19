package com.vaibhav.healthify.data.models.mapper

import com.vaibhav.healthify.data.models.local.LeaderBoardItem
import com.vaibhav.healthify.data.models.remote.UserDTO

class LeaderBoardItemMapper : Mapper<LeaderBoardItem, UserDTO> {
    override fun toEntity(dto: UserDTO) = LeaderBoardItem(
        userEmail = dto.email,
        exp = dto.exp.toInt(),
        userProfileImage = dto.profileImg,
        username = dto.username,
    )

    override fun toEntityList(dtos: List<UserDTO>) = dtos.map {
        toEntity(it)
    }

    override fun toDTO(entity: LeaderBoardItem): UserDTO {
        return UserDTO()
    }
}
