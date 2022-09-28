package auction.model;

public enum UserPermission {
    ADVERT_READ("advert:read"),
    ADVERT_ADD("advert:add"),
    ADVERT_EDIT("advert:edit"),
    ADVERT_DELETE("advert:delete"),
    ADMIN_PERMISSION("admin"),
    ADD_PRICE("add price");
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
