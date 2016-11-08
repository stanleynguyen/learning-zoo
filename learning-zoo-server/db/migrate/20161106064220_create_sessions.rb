class CreateSessions < ActiveRecord::Migration[5.0]
  def change
    create_table :sessions do |t|
      t.string :name, null: false, default: ""
      t.string :time, null: true
      t.string :lecturer_name, null: false, default: ""
      t.integer :current_slide, null: false, default: 0
      t.string :classroom, index: true #unique id the button will find
      t.jsonb :slides_sequence
      t.belongs_to :subject, foreign_key: true, index: true
    end
  end
end
