class CreateClassroom < ActiveRecord::Migration[5.0]
  def change
    create_table :classrooms do |t|
      t.string :name
      t.integer :current_session_id, index: true
    end
  end
end
