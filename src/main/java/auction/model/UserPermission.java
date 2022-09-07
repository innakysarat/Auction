package auction.model;

public enum UserPermission {
    ADVERT_READ("advert:read"),
    ADVERT_ADD("advert:add"),
    ADVERT_EDIT("advert:edit")
            ;
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
