cmake_minimum_required(VERSION 3.5)

project(chad_shell)

include_directories(${PROJECT_SOURCE_DIR}/src)

file(GLOB all_srcs
						"${PROJECT_SOURCE_DIR}/src/*.c"
						"${PROJECT_SOURCE_DIR}/src/*.h"
						)

add_executable(chadshell ${all_srcs})
