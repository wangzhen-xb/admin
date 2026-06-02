#!/usr/bin/env python3
import os
import sys
import argparse
import subprocess
from pathlib import Path

# Project root directory
PROJECT_ROOT = Path(__file__).parent.parent.parent.parent.parent

def run_dev():
    """Start development server"""
    print("Starting development server...")
    try:
        subprocess.run(
            ['pnpm', 'dev'],
            cwd=PROJECT_ROOT
        )
    except Exception as e:
        print(f"Error starting development server: {e}")

def run_build():
    """Build project"""
    print("Building project...")
    try:
        result = subprocess.run(
            ['pnpm', 'build'],
            cwd=PROJECT_ROOT,
            capture_output=True,
            text=True
        )
        if result.returncode == 0:
            print("✓ Build successful")
        else:
            print("✗ Build failed")
            print(result.stdout)
            print(result.stderr)
    except Exception as e:
        print(f"Error building project: {e}")

def run_lint():
    """Run lint"""
    print("Running lint...")
    try:
        result = subprocess.run(
            ['pnpm', 'lint'],
            cwd=PROJECT_ROOT,
            capture_output=True,
            text=True
        )
        if result.returncode == 0:
            print("✓ Lint passed")
        else:
            print("✗ Lint failed")
            print(result.stdout)
            print(result.stderr)
    except Exception as e:
        print(f"Error running lint: {e}")

def run_install():
    """Install dependencies"""
    print("Installing dependencies...")
    try:
        result = subprocess.run(
            ['pnpm', 'bootstrap'],
            cwd=PROJECT_ROOT,
            capture_output=True,
            text=True
        )
        if result.returncode == 0:
            print("✓ Dependencies installed successfully")
        else:
            print("✗ Failed to install dependencies")
            print(result.stdout)
            print(result.stderr)
    except Exception as e:
        print(f"Error installing dependencies: {e}")

def main():
    parser = argparse.ArgumentParser(description='Run development tools and commands')
    subparsers = parser.add_subparsers(dest='command', help='Command to execute')
    
    # Dev command
    dev_parser = subparsers.add_parser('dev', help='Start development server')
    
    # Build command
    build_parser = subparsers.add_parser('build', help='Build project')
    
    # Lint command
    lint_parser = subparsers.add_parser('lint', help='Run lint')
    
    # Install command
    install_parser = subparsers.add_parser('install', help='Install dependencies')
    
    args = parser.parse_args()
    
    if args.command == 'dev':
        run_dev()
    elif args.command == 'build':
        run_build()
    elif args.command == 'lint':
        run_lint()
    elif args.command == 'install':
        run_install()
    else:
        parser.print_help()

if __name__ == '__main__':
    main()
