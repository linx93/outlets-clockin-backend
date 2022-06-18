package com.outletcn.app.converter;

import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.applet.*;
import com.outletcn.app.model.mysql.Auth;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.model.mysql.Operator;
import com.outletcn.app.model.mysql.WriteOffUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 用户相关映射
 *
 * @author linx
 * @since 2022-05-16 11:11
 */
@Mapper(componentModel = "spring")
public interface UserConverter {
    @Mappings({

            @Mapping(source = "writeOffUser.birthday", target = "birthday", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "writeOffUser.id", target = "id"),
            @Mapping(source = "auth.id", target = "authId"),
    })
    UserInfo toUserInfo(WriteOffUser writeOffUser, Auth auth);


    UserInfo toUserInfo(Operator one);


    @Mappings({

            @Mapping(source = "clockInUser.birthday", target = "birthday", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "clockInUser.id", target = "id"),
            @Mapping(source = "auth.id", target = "authId"),
    })
    UserInfo toUserInfo(ClockInUser clockInUser, Auth auth);

    @Mappings({

            @Mapping(source = "clockInUser.birthday", target = "birthday", dateFormat = "yyyy/MM/dd"),
            @Mapping(source = "clockInUser.id", target = "id"),
            @Mapping(source = "clockInUser.createTime", target = "createTime"),
            @Mapping(source = "clockInUser.updateTime", target = "updateTime"),
            @Mapping(source = "auth.id", target = "authId"),
    })
    ClockInUserResponse toClockInUserResponse(ClockInUser clockInUser, Auth auth);



    @Mappings({
            @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy/MM/dd"),
    })
    ClockInUser toClockInUser(UpdateUserRequest updateUserRequest);


    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    WriteOffUser toWriteOffUser(AddWriteOffUserRequest addWriteOffUserRequest);


    Operator toOperator(NewOrModifyRequest newOrModifyRequest);

    WriteOffUser toWriteOffUser(NewOrModifyRequest newOrModifyRequest);

    List<UserManagementResponse> operatorsToUserManagementResponseList(List<Operator> operators);

    List<UserManagementResponse> writeOffUsersToUserManagementResponseList(List<WriteOffUser> writeOffUsers);

}
