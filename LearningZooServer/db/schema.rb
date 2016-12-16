# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20161127185351) do

  # These are extensions that must be enabled in order to support this database
  enable_extension "plpgsql"

  create_table "classrooms", force: :cascade do |t|
    t.string  "name"
    t.integer "current_session_id"
    t.index ["current_session_id"], name: "index_classrooms_on_current_session_id", using: :btree
  end

  create_table "sessions", force: :cascade do |t|
    t.string   "name",            default: "", null: false
    t.string   "time"
    t.string   "lecturer_name",   default: "", null: false
    t.integer  "current_slide",   default: 0,  null: false
    t.string   "classroom"
    t.jsonb    "slides_sequence"
    t.integer  "subject_id"
    t.string   "machine_key"
    t.datetime "start_date"
    t.datetime "end_date"
    t.index ["classroom"], name: "index_sessions_on_classroom", using: :btree
    t.index ["subject_id"], name: "index_sessions_on_subject_id", using: :btree
  end

  create_table "subjects", force: :cascade do |t|
    t.string "name", default: "", null: false
  end

  create_table "topics", force: :cascade do |t|
    t.string   "name",        default: "", null: false
    t.integer  "start_slide"
    t.integer  "end_slide"
    t.integer  "std_unclear", default: 0,  null: false
    t.string   "machine_key"
    t.datetime "time"
  end

  create_table "users", force: :cascade do |t|
    t.string   "provider",               default: "email", null: false
    t.string   "uid",                    default: "",      null: false
    t.string   "encrypted_password",     default: "",      null: false
    t.string   "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          default: 0,       null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip"
    t.string   "last_sign_in_ip"
    t.string   "confirmation_token"
    t.datetime "confirmed_at"
    t.datetime "confirmation_sent_at"
    t.string   "unconfirmed_email"
    t.string   "name"
    t.string   "nickname"
    t.string   "image"
    t.string   "email"
    t.json     "tokens"
    t.datetime "created_at",                               null: false
    t.datetime "updated_at",                               null: false
    t.integer  "subject_id"
    t.index ["email"], name: "index_users_on_email", using: :btree
    t.index ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true, using: :btree
    t.index ["subject_id"], name: "index_users_on_subject_id", using: :btree
    t.index ["uid", "provider"], name: "index_users_on_uid_and_provider", unique: true, using: :btree
  end

  add_foreign_key "sessions", "subjects"
  add_foreign_key "users", "subjects"
end
