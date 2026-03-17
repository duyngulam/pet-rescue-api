// ============================================================
// Pet Rescue API — Database Schema (DBML)
// Single source of truth for all tables
// ============================================================

// ── Users & Auth ──────────────────────────────────────────

Table "users" {
  "user_id" UUID [pk]
  "username" VARCHAR(255) [unique, not null]
  "email" VARCHAR(255) [unique, not null]
  "password_hash" VARCHAR(255)
  "avatar_url" TEXT
  "status" VARCHAR(50) [not null, default: 'PENDING_VERIFICATION']
  "email_verified" BOOLEAN [not null, default: false]
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ [default: `now()`]
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    status [name: "idx_users_status"]
    email [name: "idx_users_email"]
  }
}

Table "roles" {
  "role_id" SERIAL [pk, increment]
  "code" VARCHAR(100) [unique, not null]
  "name" VARCHAR(255) [not null]
  "description" TEXT
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID
}

Table "user_roles" {
  "user_id" UUID [not null]
  "role_id" INTEGER [not null]
  "assigned_at" TIMESTAMPTZ [default: `now()`]

  Indexes {
    (user_id, role_id) [pk]
    user_id [name: "idx_user_roles_user"]
    role_id [name: "idx_user_roles_role"]
  }
}

Table "permissions" {
  "permission_id" SERIAL [pk, increment]
  "resource" VARCHAR(50) [not null]
  "action" VARCHAR(50) [not null]
  "code" VARCHAR(120) [unique]
  "description" TEXT
  "created_at" TIMESTAMPTZ [not null, default: `now()`]

  Indexes {
    (resource, action) [unique]
  }
}

Table "role_permissions" {
  "role_id" INTEGER
  "permission_id" INTEGER

  Indexes {
    (role_id, permission_id) [pk]
    role_id [name: "idx_role_permissions_role"]
  }
}

Table "refresh_tokens" {
  "id" UUID [pk]
  "user_id" UUID [not null]
  "token" VARCHAR(512) [unique, not null]
  "expires_at" TIMESTAMPTZ [not null]
  "revoked" BOOLEAN [not null, default: false]
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    user_id [name: "idx_refresh_tokens_user"]
    token [name: "idx_refresh_tokens_token"]
  }
}

Table "email_verification_tokens" {
  "id" UUID [pk]
  "user_id" UUID [not null]
  "token" VARCHAR(512) [unique, not null]
  "expires_at" TIMESTAMPTZ [not null]
  "used" BOOLEAN [not null, default: false]
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID
}

Table "user_reputations" {
  "user_id" UUID [pk]
  "score" INTEGER [default: 0]
  "level" VARCHAR(50)
  "updated_at" TIMESTAMPTZ
}

// ── Organizations ─────────────────────────────────────────

Table "organizations" {
  "organization_id" UUID [pk]
  "name" VARCHAR(255)
  "type" VARCHAR(50)
  "street_address" TEXT
  "province_code" VARCHAR(20)
  "province_name" VARCHAR(255)
  "ward_code" VARCHAR(20)
  "ward_name" VARCHAR(255)
  "location" geometry(Point,4326)
  "phone" VARCHAR(50)
  "email" VARCHAR(255)
  "official_link" TEXT
  "status" VARCHAR(50)
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    type [name: "idx_org_type"]
    province_code [name: "idx_org_province"]
    (location) [type: gist, name: "idx_org_location"]
  }
}

Table "organization_members" {
  "organization_id" UUID [not null]
  "user_id" UUID [not null]
  "role" VARCHAR(30)
  "joined_at" TIMESTAMPTZ
  "status" VARCHAR(50)
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    (organization_id, user_id) [pk]
    user_id [name: "idx_org_members_user"]
  }
}

// ── Pets ──────────────────────────────────────────────────

Table "pets" {
  "pet_id" UUID [pk]
  "name" VARCHAR(255)
  "species" VARCHAR(100)
  "breed" VARCHAR(255)
  "age" INTEGER
  "gender" VARCHAR(50)
  "color" VARCHAR(255)
  "weight" NUMERIC(5,2)
  "description" TEXT
  "status" VARCHAR(50)
  "health_status" VARCHAR(30)
  "is_vaccinated" BOOLEAN [not null, default: false]
  "is_neutered" BOOLEAN [not null, default: false]
  "rescue_date" DATE
  "rescue_location" VARCHAR(255)
  "shelter_id" UUID
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    species [name: "idx_pets_species"]
    status [name: "idx_pets_status"]
    color [name: "idx_pets_color"]
    shelter_id [name: "idx_pets_shelter"]
  }
}

Table "pet_media" {
  "media_id" UUID [pk]
  "pet_id" UUID [not null]
  "url" TEXT
  "type" VARCHAR(20)
  "created_at" TIMESTAMPTZ [not null, default: `now()`]

  Indexes {
    pet_id [name: "idx_pet_media_pet"]
  }
}

Table "pet_ownerships" {
  "pet_id" UUID [not null]
  "owner_type" VARCHAR(20)
  "owner_id" UUID
  "from_time" TIMESTAMPTZ [not null]
  "to_time" TIMESTAMPTZ

  Indexes {
    (pet_id, from_time) [pk]
    owner_id [name: "idx_pet_owner_owner"]
  }
}

Table "pets_current_owner" {
  "pet_id" UUID [pk]
  "owner_type" VARCHAR(20)
  "owner_id" UUID

  Indexes {
    owner_id [name: "idx_current_owner_owner"]
  }
}

Table "pet_medical_records" {
  "record_id" UUID [pk]
  "pet_id" UUID [not null]
  "description" TEXT
  "vaccine" VARCHAR(255)
  "diagnosis" TEXT
  "record_date" TIMESTAMPTZ
  "created_by" UUID
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    pet_id [name: "idx_pet_medical_pet"]
  }
}

// ── Media Files ───────────────────────────────────────────

Table "media_files" {
  "media_id" UUID [pk]
  "uploader_id" UUID
  "public_id" VARCHAR(255) [unique, not null]
  "resource_type" VARCHAR(20)
  "format" VARCHAR(20)
  "width" INTEGER
  "height" INTEGER
  "bytes" INTEGER
  "folder" VARCHAR(255)
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    uploader_id [name: "idx_media_uploader"]
    public_id [name: "idx_media_public_id"]
  }
}

// ── Rescue Cases ──────────────────────────────────────────

Table "rescue_cases" {
  "case_id" UUID [pk]
  "reported_by" UUID
  "organization_id" UUID
  "pet_id" UUID
  "species" VARCHAR(100)
  "color" VARCHAR(100)
  "size" VARCHAR(50)
  "condition" VARCHAR(50)
  "description" TEXT
  "location" geometry(Point,4326)
  "location_text" TEXT
  "province_code" VARCHAR(50)
  "ward_code" VARCHAR(50)
  "status" VARCHAR(50)
  "reported_at" TIMESTAMPTZ
  "resolved_at" TIMESTAMPTZ
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    (location) [type: gist, name: "idx_rescue_location"]
    status [name: "idx_rescue_status"]
    species [name: "idx_rescue_species"]
    reported_at [name: "idx_rescue_reported_at"]
  }
}

Table "rescue_media" {
  "case_id" UUID [not null]
  "media_id" UUID [not null]

  Indexes {
    (case_id, media_id) [pk]
  }
}

// ── Adoption ──────────────────────────────────────────────

Table "adoption_applications" {
  "application_id" UUID [pk]
  "pet_id" UUID
  "applicant_id" UUID
  "organization_id" UUID
  "status" VARCHAR(50)
  "note" TEXT
  "experience" TEXT
  "live_condition" TEXT
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "decided_at" TIMESTAMPTZ
  "decided_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    pet_id [name: "idx_adoption_pet"]
    applicant_id [name: "idx_adoption_applicant"]
    status [name: "idx_adoption_status"]
  }
}

// ── Posts & Media ─────────────────────────────────────────

Table "posts" {
  "post_id" UUID [pk]
  "author_id" UUID
  "rescue_case_id" UUID
  "content" TEXT
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID

  Indexes {
    author_id [name: "idx_posts_author"]
    rescue_case_id [name: "idx_posts_rescue"]
    created_at [name: "idx_posts_created_at"]
  }
}

Table "post_media" {
  "post_id" UUID [not null]
  "media_id" UUID [not null]

  Indexes {
    (post_id, media_id) [pk]
  }
}

// ── Tags ──────────────────────────────────────────────────

Table "tags" {
  "tag_id" UUID [pk]
  "code" VARCHAR(100) [unique]
  "name" VARCHAR(255)
  "description" TEXT
  "created_at" TIMESTAMPTZ [not null, default: `now()`]
  "created_by" UUID
  "updated_at" TIMESTAMPTZ
  "updated_by" UUID
  "is_deleted" BOOLEAN [not null, default: false]
  "deleted_at" TIMESTAMPTZ
  "deleted_by" UUID
}

Table "post_tags" {
  "post_id" UUID [not null]
  "tag_id" UUID [not null]

  Indexes {
    (post_id, tag_id) [pk]
    tag_id [name: "idx_post_tags_tag"]
  }
}

// ── Audit Logs ────────────────────────────────────────────

Table "audit_logs" {
  "log_id" UUID [pk]
  "entity_type" VARCHAR(100) [not null]
  "entity_id" VARCHAR(100) [not null]
  "action" VARCHAR(50) [not null]
  "actor_id" UUID
  "old_value" JSONB
  "new_value" JSONB
  "ip_address" VARCHAR(50)
  "created_at" TIMESTAMPTZ [not null, default: `now()`]

  Indexes {
    (entity_type, entity_id) [name: "idx_audit_entity"]
    actor_id [name: "idx_audit_actor"]
    created_at [name: "idx_audit_created"]
  }
}

// ── Foreign Keys ──────────────────────────────────────────

Ref:"users"."user_id" < "user_roles"."user_id" [delete: cascade]
Ref:"roles"."role_id" < "user_roles"."role_id" [delete: cascade]
Ref:"roles"."role_id" < "role_permissions"."role_id" [delete: cascade]
Ref:"permissions"."permission_id" < "role_permissions"."permission_id" [delete: cascade]
Ref:"users"."user_id" < "user_reputations"."user_id"
Ref:"users"."user_id" < "refresh_tokens"."user_id" [delete: cascade]
Ref:"users"."user_id" < "email_verification_tokens"."user_id" [delete: cascade]
Ref:"users"."user_id" < "organizations"."created_by"
Ref:"organizations"."organization_id" < "organization_members"."organization_id" [delete: cascade]
Ref:"users"."user_id" < "organization_members"."user_id" [delete: cascade]
Ref:"pets"."pet_id" < "pet_media"."pet_id" [delete: cascade]
Ref:"pets"."pet_id" < "pet_ownerships"."pet_id"
Ref:"pets"."pet_id" < "pets_current_owner"."pet_id"
Ref:"pets"."pet_id" < "pet_medical_records"."pet_id" [delete: cascade]
Ref:"users"."user_id" < "pet_medical_records"."created_by"
Ref:"users"."user_id" < "media_files"."uploader_id"
Ref:"users"."user_id" < "rescue_cases"."reported_by"
Ref:"organizations"."organization_id" < "rescue_cases"."organization_id"
Ref:"pets"."pet_id" < "rescue_cases"."pet_id"
Ref:"rescue_cases"."case_id" < "rescue_media"."case_id" [delete: cascade]
Ref:"media_files"."media_id" < "rescue_media"."media_id" [delete: cascade]
Ref:"pets"."pet_id" < "adoption_applications"."pet_id"
Ref:"users"."user_id" < "adoption_applications"."applicant_id"
Ref:"organizations"."organization_id" < "adoption_applications"."organization_id"
Ref:"users"."user_id" < "adoption_applications"."decided_by"
Ref:"users"."user_id" < "posts"."author_id"
Ref:"rescue_cases"."case_id" < "posts"."rescue_case_id"
Ref:"posts"."post_id" < "post_media"."post_id" [delete: cascade]
Ref:"media_files"."media_id" < "post_media"."media_id" [delete: cascade]
Ref:"posts"."post_id" < "post_tags"."post_id" [delete: cascade]
Ref:"tags"."tag_id" < "post_tags"."tag_id" [delete: cascade]
Ref:"users"."user_id" < "audit_logs"."actor_id"
