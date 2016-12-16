class ClassroomsController < ApplicationController
  before_action :prepare_classroom, only: [:show, :update, :destroy]

  def index
    @classrooms = Classroom.all
    render json: @classrooms
  end

  def show
    render json: @classroom
  end

  def create
    @classroom = Classroom.new(classroom_params)
    if @classroom.save
      render json: @classroom
    else
      render json: { errors: @classroom.errors.full_messages }
    end
  end

  def update
    if @classroom.update(classroom_params)
      render json: @classroom
    else
      render json: { errors: @classroom.errors.full_messages }
    end
  end

  def destroy
    @classroom.destroy
    render json: { success: "true" }
  rescue ActiveRecord::InvalidForeignKey
    render json: { errors: "this classroom is in use by other things" }
  end

  private

  def prepare_classroom
    @classroom = Classroom.find(params[:id])
  end

  def classroom_params
    params.require(:classroom).permit(:name, :current_session_id)
  end
end
