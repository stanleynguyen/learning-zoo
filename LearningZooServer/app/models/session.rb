class Session < ActiveRecord::Base
  belongs_to :subject, inverse_of: :sessions

  validates :name, presence: true
  validates :machine_key, :start_date, :end_date, presence: true
end
