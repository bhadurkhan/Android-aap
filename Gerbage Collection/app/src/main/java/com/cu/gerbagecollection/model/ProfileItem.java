package com.cu.gerbagecollection.model;

public class ProfileItem {
    private String UserId,
            Name,
            Email,
            Password,
            Status ,
            RoleId ,
            RoleName ,
            ContactNo,
            Available;

    public ProfileItem(String userId, String name, String email, String password, String status, String roleId, String roleName, String contactNo, String available) {
        UserId = userId;
        Name = name;
        Email = email;
        Password = password;
        Status = status;
        RoleId = roleId;
        RoleName = roleName;
        ContactNo = contactNo;
        Available = available;
    }

    public String getUserId() {
        return UserId;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getStatus() {
        return Status;
    }

    public String getRoleId() {
        return RoleId;
    }

    public String getRoleName() {
        return RoleName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public String getAvailable() {
        return Available;
    }
}
