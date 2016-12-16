class SubjectsController < ApplicationController
  before_action :prepare_subject, only: [:show, :update, :destroy]

  def index
    @subjects = Subject.all
    render json: @subjects
  end

  def show
    render json: @subject
  end

  def create
    @subject = Subject.new(subject_params)
    if @subject.save
      render json: @subject
    else
      render json: { errors: @subject.errors.full_messages }
    end
  end

  def update
    if @subject.update(subject_params)
      render json: @subject
    else
      render json: { errors: @subject.errors.full_messages }
    end
  end

  def destroy
    @subject.destroy
    render json: { success: "true" }
  rescue ActiveRecord::InvalidForeignKey
    render json: { errors: "this subject is in use by other things" }
  end

  private

  def prepare_subject
    @subject = Subject.find(params[:id])
  end

  def subject_params
    params.require(:subject).permit(:name)
  end
end
