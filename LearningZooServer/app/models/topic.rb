class Topic < ActiveRecord::Base
  validates :machine_key, :time, presence: true
end
