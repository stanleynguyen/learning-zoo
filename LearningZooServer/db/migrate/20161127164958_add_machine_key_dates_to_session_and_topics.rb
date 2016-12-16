class AddMachineKeyDatesToSessionAndTopics < ActiveRecord::Migration[5.0]
  def change
    add_column :sessions, :machine_key, :string, index: true
    add_column :sessions, :start_date, :datetime
    add_column :sessions, :end_date, :datetime

    add_column :topics, :machine_key, :string, index: true
    add_column :topics, :time, :datetime
    remove_column :topics, :session_id, :integer
  end
end
