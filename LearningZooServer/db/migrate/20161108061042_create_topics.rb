class CreateTopics < ActiveRecord::Migration[5.0]
  def change
    create_table :topics do |t|
      t.string :name, null: false, default: ""
      t.integer :start_slide, null: true
      t.integer :end_slide, null: true
      t.integer :std_unclear, null: false, default: 0
      t.belongs_to :session, foreign_key: true, index: true
    end
  end
end
