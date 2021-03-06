variable "access_key" {}
variable "secret_key" {}
variable "key_name" {}
variable "private_key_path" {}

variable "instance_type" {
  default = "t2.micro"
}

provider "aws" {
  region = "ca-central-1"
}

variable "aws_amis" {
  default = {
    eu-west-1 = "ami-674cbc1e"
    us-east-1 = "ami-2757f631"
    us-west-2 = "ami-8803e0f0"
    ca-central-1 = "ami-a954d1cd"
  }
}
