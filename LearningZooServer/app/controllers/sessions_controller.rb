class SessionsController < ApplicationController
  before_action :prepare_session, only: [:show, :update, :destroy, :get_topics]

  def index
    @sessions = Session.all
    render json: @sessions
  end

  def show
    render json: @session
  end

  def create
    @session = Session.new(session_params)
    if @session.save
      render json: @session
    else
      render json: { errors: @session.errors.full_messages }
    end
  end

  def update
    if @session.update(session_params)
      render json: @session
    else
      render json: { errors: @session.errors.full_messages }
    end
  end

  def destroy
    @session.destroy
    render json: { success: "true" }
  rescue ActiveRecord::InvalidForeignKey
    render json: { errors: "this session is in use by other things" }
  end

  def get_topics
    @topics = Topic.where(["machine_key = ? and time > ? and time < ?", @session.machine_key, @session.start_date, @session.end_date])
    render json: @topics
  end

  private

  def prepare_session
    @session = Session.find(params[:id])
  end

  def session_params
    params.require(:session).permit(:name, :time, :lecturer_name, :current_slide, :classroom, :slides_sequence, :subject_id, :machine_key, :start_date, :end_date)
  end
end
